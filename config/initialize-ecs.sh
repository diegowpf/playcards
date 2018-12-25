terraform apply -auto-approve -target=module.network
terraform apply -auto-approve -target=module.ecr
terraform apply -auto-approve -target=module.ecs
./push-images.sh
terraform apply -auto-approve
