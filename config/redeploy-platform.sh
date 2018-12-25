./push-images.sh
terraform destroy -target=module.builder-of -force
terraform destroy -target=module.firehose -force
terraform destroy -target=module.insite-knowledge -force
terraform destroy -target=module.contracts-with -force
terraform destroy -target=module.companies -force
terraform destroy -target=module.client -force

terraform apply -auto-approve
