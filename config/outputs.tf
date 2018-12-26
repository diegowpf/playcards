output "name_servers" {
        value = "${aws_route53_zone.primary.name_servers}"
}

output "server_dns_name" {
  value = "${module.server.dns_name}"
}
