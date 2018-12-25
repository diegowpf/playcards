provider "aws" {
  region = "${var.region}"
}

resource "aws_route53_zone" "primary" {
  name = "immersivesports.ai"
}

# Fetch AZs in the current region
data "aws_availability_zones" "available" {}

module "client" {
  source = "./modules/container"

  execution_role_arn = "${module.ecr.execution_role_arn}"
  cluster_id         = "${module.ecs.ecs_cluster_id}"
  vpc_id             = "${module.network.vpc_id}"
  private_subnets    = "${module.network.private_subnets}"
  public_subnets     = "${module.network.public_subnets}"
  docker_image       = "068681799287.dkr.ecr.us-east-1.amazonaws.com/playcards-client"
  container_family   = "client"
  health_check_path  = "/"
  container_port     = 3000
  loadbalancer_port  = 80

  instance_count     = 1
  timeout            = 180
  zone_id = "${aws_route53_zone.primary.zone_id}"
}

module "server" {
  source = "./modules/container"

  execution_role_arn = "${module.ecr.execution_role_arn}"
  cluster_id         = "${module.ecs.ecs_cluster_id}"
  vpc_id             = "${module.network.vpc_id}"
  private_subnets    = "${module.network.private_subnets}"
  public_subnets     = "${module.network.public_subnets}"
  docker_image       = "068681799287.dkr.ecr.us-east-1.amazonaws.com/playcards-server"
  container_family   = "server"
  # memory             = 4096
  # cpu                = 2048
  instance_count     = 2
  timeout            = 180
  container_port     = 8080
  zone_id = "${aws_route53_zone.primary.zone_id}"
}

module "network" {
  source = "./modules/networking"

  cidr_block = "${var.cidr_block}"
}

module "ecs" {
  source           = "./modules/ecs"
  ecs_cluster_name = "immersivesports"

  vpc_id          = "${module.network.vpc_id}"
  private_subnets = "${module.network.private_subnets}"
  public_subnets  = "${module.network.public_subnets}"
  zone_id = "${aws_route53_zone.primary.zone_id}"
}

module "ecr" {
  source = "./modules/ecr"
}
