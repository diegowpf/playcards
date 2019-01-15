resource "aws_cloudwatch_log_group" "container" {
  name = "${var.container_family}"

  tags {
    Environment = "production"
  }
}

resource "aws_ecs_task_definition" "service" {
  family                   = "${var.container_family}"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "${var.cpu}"
  memory                   = "${var.memory}"
  execution_role_arn       = "${var.execution_role_arn}"

  container_definitions = <<DEFINITION
[
 {
   "cpu": ${var.cpu},
   "memory": ${var.memory},
   "name": "${var.container_family}",
   "image": "${var.docker_image}",
   "networkMode": "awsvpc",
   "portMappings": [
     {
       "containerPort": ${var.container_port},
       "hostPort": ${var.container_port}
     }
   ],
   "logConfiguration": {
                "logDriver": "awslogs",
                "options": {
                    "awslogs-group": "${aws_cloudwatch_log_group.container.name}",
                    "awslogs-region": "us-east-1",
                    "awslogs-stream-prefix": "logs"
                }
            },
   "environment" : [
     { "name": "REACT_APP_SERVER_URL", "value" :"${var.REACT_APP_SERVER_URL}"},
     { "name": "PERSISTENCE_MONGO_PORT", "value" :"${var.PERSISTENCE_MONGO_PORT}"},
     { "name": "PERSISTENCE_MONGO_URL", "value" :"${var.PERSISTENCE_MONGO_URL}"}
   ]
 }
]
DEFINITION
}

resource "aws_ecs_service" "service" {
  name          = "${var.container_family}"
  cluster       = "${var.cluster_id}"
  desired_count = "${var.instance_count}"

  launch_type = "FARGATE"
  depends_on  = ["aws_alb_target_group.front_end", "aws_alb.lb"]

  # Track the latest ACTIVE revision
  task_definition = "${aws_ecs_task_definition.service.family}:${max("${aws_ecs_task_definition.service.revision}", "${aws_ecs_task_definition.service.revision}")}"

  network_configuration {
    security_groups = ["${aws_security_group.lb.id}"]
    subnets         = ["${var.private_subnets}"]
  }

  load_balancer {
    target_group_arn = "${aws_alb_target_group.front_end.id}"
    container_name   = "${var.container_family}"
    container_port   = "${var.container_port}"
  }
}

resource "aws_alb" "lb" {
  security_groups            = ["${aws_security_group.lb.id}"]
  subnets                    = ["${var.public_subnets}"]
  enable_deletion_protection = false
  idle_timeout  = "${var.timeout}"

  tags {
    Environment = "production"
  }
}

resource "aws_alb_target_group" "front_end" {
  port        = "${var.container_port}"
  protocol    = "HTTP"
  vpc_id      = "${var.vpc_id}"
  target_type = "ip"

  health_check {
    path     = "${var.health_check_path}"
    matcher  = "${var.matcher_ports}"
    interval = "${var.timeout + 10}"
  }
}

resource "aws_alb_listener" "front_end" {
  load_balancer_arn = "${aws_alb.lb.id}"
  port              = "${var.loadbalancer_port}"
  protocol          = "HTTP"

  default_action {
    target_group_arn = "${aws_alb_target_group.front_end.id}"
    type             = "forward"
  }
}

# ALB Security group
# This is the group you need to edit if you want to restrict access to your application
resource "aws_security_group" "lb" {
  description = "controls access to the ALB"
  vpc_id      = "${var.vpc_id}"

  ingress {
    protocol    = "tcp"
    from_port   = "${var.loadbalancer_port}"
    to_port     = "${var.container_port}"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}


resource "aws_route53_record" "www" {
  zone_id = "${var.zone_id}"
  name    = "${var.container_family}.${var.base_domain}"
  type    = "CNAME"
  ttl     = "300"
  records = ["${aws_alb.lb.dns_name}"]
}
