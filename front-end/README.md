# EPI-FOOS



## Backend
Create database with name `epifoos` and username and password `postgres:postgres`. Open in Intellij
and run through Intellij

## Frontend
Add `.env` file in front-end folder with following contents:
```bash
PUBLIC_HOST=http://localhost:8080
PUBLIC_ENV=DEV
PUBLIC_DOMAIN=localhost
```

## Deployment to new server

### Backend

Get new AWS ECR Token `aws ecr get-login-password`\
Build backend image `./gradlew buildFatJar --no-daemon`\
Build docker image `docker build -t epifoos-backend:latest`
Push docker image `docker push 051511754184.dkr.ecr.af-south-1.amazonaws.com/epifoos-backend`

### Frontend

Change directory `cd front-end`
Get new AWS ECR Token `aws ecr get-login-password`\
Build npm `npm run build && npm prune --production`\
Build docker image `docker build -t epifoos-frontend:latest`
Push docker image `docker push 051511754184.dkr.ecr.af-south-1.amazonaws.com/epifoos-frontend`

### EC2 Server
SSH into server `ssh user@ip`
Install docker `sudo yum install docker -y`\
Install docker-compose
```
sudo curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose version
```
Create `docker-compose.yml` file with contents of `deployment/docker-compose.yml`\
Create `assets` folder
Create `default.conf` file with either ssl (follow certbot) or no ssl (from `deployment` folder)
Create `.env-frontend` file
```
#.env-frontend
PUBLIC_ENV=PROD
PUBLIC_HOST=https://host.com
PUBLIC_DOMAIN=host.com
```
Create `.env-backend` file
```
#.env-backend
PORT=8080
ENV=PROD

DATABASE.URL=dbUrl
DATABASE.USER=dbUsername
DATABASE.PASSWORD=dbPassword

JWT.SECRET=generatedSecret
JWT.ISSUER=https://host.com
JWT.AUDIENCE=https://host.com/api/
```

Sign into ECR `$(aws ecr get-login --region af-south-1 --no-include-email)`\
Create script `start-server.sh` and make it executable `chmod +x start-server.sh`
```
#start-server.sh
docker-compose -f docker-compose.yml down
$(aws ecr get-login --region af-south-1 --no-include-email)
docker pull 051511754184.dkr.ecr.af-south-1.amazonaws.com/epifoos-frontend:latest
docker pull 051511754184.dkr.ecr.af-south-1.amazonaws.com/epifoos-backend:latest
docker-compose -f docker-compose.yml up --build --force-recreate -d
```
Run `./start-server.sh`

### Certbot
Before starting the server, copy contents of `deployment default.conf.ssl` to `default.conf`\
Run `docker-compose up`, ensuring nginx and Certbot starts up\
In another window, run `docker compose run --rm  certbot certonly --webroot --webroot-path /var/www/certbot/ -d example.org`\
Stop the `docker-compose up` instance.\
Edit `default.conf` and remove the comments\
Start the server again with `./start-server.sh`

#### Renewing Certificate
`docker compose run --rm certbot renew`
