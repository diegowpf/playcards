eval "$(aws ecr get-login --no-include-email --region us-east-1)"
DOCKER_REPO_URL=068681799287.dkr.ecr.us-east-1.amazonaws.com

docker push ${DOCKER_REPO_URL}/playcards-client
docker push ${DOCKER_REPO_URL}/playcards-server
