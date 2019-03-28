variable "ecs_cluster_name" {
  default = "bytecubed"
}

variable "private_subnets" {
  type = "list"
}

variable "public_subnets" {
  type = "list"
}

variable "vpc_id" {}

variable "base_domain" {
  default = "platform.bytecubedlabs.co"
}

variable "zone_id" {}
