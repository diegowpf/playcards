eval "$(aws ecr get-login --no-include-email --region us-east-1)"
DOCKER_REPO_URL=068681799287.dkr.ecr.us-east-1.amazonaws.com

docker tag bytecubedlabs/playcards-client ${DOCKER_REPO_URL}/playcards-client
docker tag bytecubedlabs/playcards-server ${DOCKER_REPO_URL}/playcards-server
docker tag bytecubedlabs/playcards-nlp ${DOCKER_REPO_URL}/playcards-nlp
docker tag bytecubedlabs/playcards-teams ${DOCKER_REPO_URL}/playcards-teams

docker push ${DOCKER_REPO_URL}/playcards-client
docker push ${DOCKER_REPO_URL}/playcards-server
docker push ${DOCKER_REPO_URL}/playcards-nlp
docker push ${DOCKER_REPO_URL}/playcards-teams
