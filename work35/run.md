
## Getting Started

Just run the Application.java in your IDE.  
Application is running on http://localhost:9080.


### Step 1
Use docker-compose to start Grafana and Prometheus servers.
- In the root folder
```sh
docker-compose -f docker-compose.yml up
```

### Step 2
Check the Prometheus server.
- Open http://localhost:9090
- Access status -> Targets, both endpoints must be "UP"

### Step 3
Configure the Grafana.
- Open http://localhost:3000
- **Configure integration with Prometheus**
    - Access configuration
    - Add data source
    - Select Prometheus
    - Use url "http://localhost:9090" and access with value "Browser"
