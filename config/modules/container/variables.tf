variable "execution_role_arn" {}
variable "cluster_id" {}
variable "vpc_id" {}

variable "private_subnets" {
  type = "list"
}

variable "public_subnets" {
  type = "list"
}

variable "docker_image" {}
variable "container_family" {}

variable "instance_count" {
  default = 1
}

variable "container_port" {
  default = 8080
}

variable "loadbalancer_port" {
  default = 80
}

variable "cpu" {
  default = 256
}

variable "memory" {
  default = 512
}

variable "health_check_path" {
  default = "/actuator/health"
}

variable "matcher_ports" {
  default = "200,302"
}

variable "timeout" {
  default = 60
}

variable "REACT_APP_SERVER_URL" {
  default = "foo"
}

variable "PERSISTENCE_MONGO_URL" {
  default = "infra.platform.bytecubedlabs.co"
}

variable "PERSISTENCE_MONGO_PORT" {
  default = 27017
}

variable "base_domain" {
  default = "platform.bytecubedlabs.co"
}

variable "zone_id" {}
