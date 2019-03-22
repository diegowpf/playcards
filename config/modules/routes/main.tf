resource "aws_route53_record" "records" {
  zone_id = "${aws_route53_zone.immersive.zone_id}"
  name    = "michigan.immersivesports.ai"
  type    = "A"
  ttl     = "300"
  records = ["40.117.45.119"]
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

resource "aws_route53_record" "updater" {
  zone_id = "${var.zone_id}"
  name    = "updates.platform.bytecubedlabs.co"
  type    = "A"
  ttl     = "300"
  records = ["13.68.140.33"]
}

resource "aws_route53_record" "dev_backend" {
  zone_id = "${var.zone_id}"
  name    = "dev-api.platform.bytecubedlabs.co"
  type    = "A"
  ttl     = "300"
  records = ["13.68.140.33"]
}

resource "aws_route53_record" "demo_backend" {
  zone_id = "${var.zone_id}"
  name    = "demo-api.platform.bytecubedlabs.co"
  type    = "A"
  ttl     = "300"
  records = ["54.209.115.220"]
}

resource "aws_route53_zone" "immersive" {
   name = "immersivesports.ai"
}
