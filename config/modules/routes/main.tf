resource "aws_route53_record" "records" {
  zone_id = "${var.zone_id}"
  name    = "michigan.immersivesports.ai"
  type    = "A"
  ttl     = "300"
  #records = ["40.117.45.119"]
  records = ["40.71.176.103"]
}

resource "aws_route53_record" "zero" {
  zone_id = "${var.zone_id}"
  name    = "zero.immersivesports.ai"
  type    = "A"
  ttl     = "300"
  records = ["40.113.201.39"]
}

resource "aws_route53_record" "zeros" {
  zone_id = "${var.zone_id}"
  name    = "zeros.immersivesports.ai"
  type    = "A"
  ttl     = "300"
  records = ["40.113.201.39"]
}
