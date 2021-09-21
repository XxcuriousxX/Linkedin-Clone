# LinkeDIT

## 1. Install

### 1.1. front-end

#### 1.1.1. locally

```sh
cd front-end
npm install
npm run start
```

#### 1.1.2. on docker

```sh
cd front-end
chmod +x ./frontend.sh
./frontend.sh
```

### 1.2. back-end

#### 1.2.1. database on docker, spring boot app on intellij IDEA
```sh
cd back-end/docker-sql
docker-compose up -d
```
shutdown with
```sh
docker-compose down
```

#### 1.2.2. database and spring boot app on docker

```sh
cd back-end
chmod +x ./backend.sh
./backend.sh
```
In case of editing the backend, then you must
re-compile with "clean" and "install" life cycles,
for the changes to take effect for the springboot 
application on docker. 