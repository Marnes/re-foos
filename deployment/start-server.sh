$(aws ecr get-login --region af-south-1 --no-include-email)
docker pull 051511754184.dkr.ecr.af-south-1.amazonaws.com/epifoos-frontend:latest
docker pull 051511754184.dkr.ecr.af-south-1.amazonaws.com/epifoos-backend:latest

docker-compose build
docker-compose down
docker-compose up --force-recreate -d
