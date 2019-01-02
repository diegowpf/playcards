# Simply specify the family to find the latest ACTIVE revision in that family.
# data "aws_ecs_task_definition" "mongo" {
#   task_definition = "${aws_ecs_task_definition.mongo.family}"
# }

/*resource "aws_ecs_cluster" "mongo-cluster" {
  name = "mongo-cluster"
}*/

resource "aws_ecs_task_definition" "mongo" {
  family = "mongo"

  container_definitions = <<DEFINITION
[
  {
    "cpu": 128,
    "environment": [{
      "name": "SECRET",
      "value": "KEY"
    }],
    "essential": true,
    "image": "mongo:latest",
    "memory": 128,
    "memoryReservation": 64,
    "name": "mongo",
    "portMappings": [
      {
        "containerPort": 27017,
        "hostPort": 27017
      }
    ]
  }
]
DEFINITION
}

resource "aws_ecs_service" "mongo" {
  name          = "mongo"
  cluster       = "${var.cluster_id}"
  desired_count = 1

  # Track the latest ACTIVE revision
  task_definition = "${aws_ecs_task_definition.mongo.family}:${max("${aws_ecs_task_definition.mongo.revision}", "${aws_ecs_task_definition.mongo.revision}")}"
}
