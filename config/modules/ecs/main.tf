resource "aws_ecs_cluster" "infrastructure" {
  name = "${var.ecs_cluster_name}"
}

resource "aws_iam_role" "ecs-service-role" {
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "ecs.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "ecs-service-role-attachment" {
  role       = "${aws_iam_role.ecs-service-role.name}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceRole"
}

resource "aws_instance" "compute" {
  # ECS-optimized AMI for us-east-1
  ami           = "ami-cb2305a1"
  instance_type = "m4.xlarge"
  count         = 1

  user_data = <<EOF
#!/bin/bash
echo ECS_CLUSTER=${var.ecs_cluster_name} >> /etc/ecs/ecs.config
EOF

  iam_instance_profile   = "${aws_iam_instance_profile.compute.name}"
  vpc_security_group_ids = ["${aws_security_group.compute.id}"]
  subnet_id              = "${var.public_subnets[0]}"

  associate_public_ip_address = true
}

//MongoDB Security Group
resource "aws_security_group" "compute" {
  name        = "compute"
  vpc_id      = "${var.vpc_id}"
  description = "Allow all inbound traffic from VPC and SSH from world"

  # tags { Name = "${var.name}-mongodb" }
  lifecycle {
    create_before_destroy = true
  }

  ingress {
    protocol    = "tcp"
    from_port   = 7474
    to_port     = 7474
    cidr_blocks = ["0.0.0.0/0"]

    # cidr_blocks = ["${var.vpc-cidr}"]
  }

  ingress {
    protocol    = "tcp"
    from_port   = 7687
    to_port     = 7687
    cidr_blocks = ["0.0.0.0/0"]

    # cidr_blocks = ["${var.vpc-cidr}"]
  }

  ingress {
    protocol    = "tcp"
    from_port   = 5672
    to_port     = 5672
    cidr_blocks = ["0.0.0.0/0"]

    # cidr_blocks = ["${var.vpc-cidr}"]
  }

  ingress {
    protocol    = "tcp"
    from_port   = 15672
    to_port     = 15672
    cidr_blocks = ["0.0.0.0/0"]

    # cidr_blocks = ["${var.vpc-cidr}"]
  }

  ingress {
    protocol    = "tcp"
    from_port   = 27017
    to_port     = 27017
    cidr_blocks = ["0.0.0.0/0"]

    # cidr_blocks = ["${var.vpc-cidr}"]
  }

  egress {
    protocol    = -1
    from_port   = 0
    to_port     = 0
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# ami-5253c32d

resource "aws_iam_role" "compute" {
  # name = "mongo"
  assume_role_policy = <<EOF
{
"Version": "2012-10-17",
"Statement": [
  {
    "Effect": "Allow",
    "Principal": {
      "Service": "ec2.amazonaws.com"
    },
    "Action": "sts:AssumeRole"
  }
]
}
EOF
}

resource "aws_iam_role_policy" "compute" {
  # name = "ecs_instance_role"
  role = "${aws_iam_role.compute.id}"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ecs:CreateCluster",
        "ecs:DeregisterContainerInstance",
        "ecs:DiscoverPollEndpoint",
        "ecs:Poll",
        "ecs:RegisterContainerInstance",
        "ecs:StartTelemetrySession",
        "ecs:Submit*",
        "ecr:GetAuthorizationToken",
        "ecr:BatchCheckLayerAvailability",
        "ecr:GetDownloadUrlForLayer",
        "ecr:BatchGetImage",
        "ecs:StartTask"
      ],
      "Resource": "*"
    }
  ]
}
EOF
}

resource "aws_iam_instance_profile" "compute" {
  # name = "mongo-profile"
  role = "${aws_iam_role.compute.name}"
}

resource "aws_route53_record" "www" {
  zone_id = "${var.zone_id}"
  name    = "infra.${var.base_domain}"
  type    = "A"
  ttl     = "300"
  records = ["${aws_instance.compute.public_ip}"]
}
