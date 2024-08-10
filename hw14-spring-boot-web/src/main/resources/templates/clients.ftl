<html lang="en" xmlns:th="http://www.thymeleaf.org">
<!DOCTYPE html>
<head>
    <title>Клиенты</title>

    <script>
        function createClient() {
            const name = document.getElementById('clientName');
            const address = document.getElementById('clientAddress');
            const phone = document.getElementById('clientPhone');

            console.log(name.value)

            fetch('/api/clients', {
                method: 'POST',
                body: JSON.stringify({
                    name: name.value,
                    address: address.value,
                    phone: phone.value,
                }),
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error!`);
                    }
                    return response.text();
                })
                .then(data => {
                    console.log(data);
                    window.location.href = "/clients/";
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
    </script>
</head>

<h4>Создать клиента</h4>

<form id="createClientForm" method="post">
    <label>
        Имя клиента <br/>
        <input id="clientName" type="text" name="name" value="Иван">
    </label>
    <br/>
    <label>
        Улица<br/>
        <input id="clientAddress" type="text" name="street" value="ул. Ленина">
    </label>
    <br/>
    <label>
        Телефон<br/>
        <input id="clientPhone" type="text" name="number" value="+7911100000000">
    </label>
    <br/>
    <input type="button" onclick="createClient()" value="Создать клиента">
</form>

<h4>Список клиентов</h4>

<table style="width: 400px">
    <thead>
        <tr>
            <td style="width: 50px">Id</td>
            <td style="width: 150px">Имя</td>
            <td style="width: 100px">Улица</td>
            <td style="width: 100px">Номер телефона</td>
        </tr>
    </thead>
    <tbody>
    <tr th:each="client : ${clients}">
        <td th:text="${client.id}">1</td>
        <td th:text="${client.name}">John Doe</td>
        <td th:text="${client.address.street}"></td>
        <td th:text="${client.phones[0].number}"></td>
    </tr>
    </tbody>
</table>
</body>
</html>
