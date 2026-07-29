# Job App Frontend

Browser UI for the Spring microservices in this repository.

## Run

Start the backend services first:

1. Eureka server on `8761`
2. Company service on `8082`
3. Job service on `8081`
4. Review service on `8083`
5. Gateway on `8084`

Then serve this folder:

```powershell
cd Frontend
python -m http.server 5173
```

Open `http://localhost:5173`.

The frontend calls the backend through the gateway at `http://localhost:8084`. You can change that from the API base field in the UI.
