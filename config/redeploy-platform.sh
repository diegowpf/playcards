./build-platform.sh
./push-images.sh

terraform destroy -target=module.client -force
terraform destroy -target=module.server -force

terraform apply -auto-approve
