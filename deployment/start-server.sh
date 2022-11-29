docker-compose -f docker-compose.yml down
$(aws ecr get-login --region af-south-1 --no-include-email)
docker pull 051511754184.dkr.ecr.af-south-1.amazonaws.com/epifoos-frontend:latest
docker pull 051511754184.dkr.ecr.af-south-1.amazonaws.com/epifoos-backend:latest
docker-compose -f docker-compose.yml up --build --force-recreate -d
