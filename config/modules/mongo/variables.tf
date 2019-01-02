variable "cluster_id" {}
variable "ecs_cluster_name" {}

variable "private_subnets" {
  type = "list"
}

variable "public_subnets" {
  type = "list"
}

variable "vpc_id" {}
variable "vpc_cidr" {}
