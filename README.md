# Excercise instructions:

Utilize o Github para entregar o desafio proposto.
Lembre-se de fazer commit do passo-a-passo do desenvolvimento, pois a utilização do Github tambem será avaliada.
Escreva no README o passo-a-passo para testar a aplicação.
O importante é entregar com qualidade, mesmo que nem tudo funcione.
Após finalizar, envie o link do repositório.

Desenvolva APIs que permitam incluir, alterar, deletar e consultar agendamento de transações bancárias.
Para cada transação, uma taxa de transferencia deve ser cobrada da seguinte forma:

Taxa A (valor da transferencia entre 0€ e 1000€)
- Data do Agendamento igual Data Atual - 3% do valor da transação + 3€

Taxa B (valor da transferencia entre 1001€ e 2000€)
- Data de agendamento entre 1 e 10 dias da data atual - 9%

Taxa C (valor da transferencia maior que 2000€)
- Data de agendamento entre 11 e 20 dias da data atual - 8.2% do valor da transação
- Data de agendamento entre 21 e 30 dias da data atual - 6.9% do valor da transação
- Data de agendamento entre 31 e 40 dias da data atual - 4.7% do valor da transação
- Data de agendamento maior que 40 dias da data atual - 1.7% do valor da transação

Testes unitários não são obrigatórios, mas são bem-vindos.

# Testing instructions
1- Clone the repo and open in intellij idea

2- Certify that MySQL and MySQL Workbench is installed. Go to MySQL Workbench and add a new databse with the name 'banking_app' , as is defined in the application.properties file

3- Modify password and user values on the application.properties to the correct credentials of your MySQL

4- Run the BankTransactionsApplication @SpringBootApplication entrypoint to start the application

5- To create and modify Transactions is necessary a JSON object with 2 properties,  a date value and an amount value

6- If the date values on the JSON object sent are not compatible with the date values defined in the instructions, a message will be returned in the Status Response, with a tip to correct the date.

# Testing the endpoints:
- Open Postman to test the endpoints, and change the Request verb and url according to the test specified.

- Creating transactions with different fees:

1 - Test 1

POST url:  localhost:9090/api/transactions

JSON: {
"schedulingDate": "2024-02-28",
"amount": 60.00
}


2 - Test 2

POST url:  localhost:9090/api/transactions

JSON: {
"schedulingDate": "2024-03-04",
"amount": 1050.00
}

3 - Test 3

POST url:  localhost:9090/api/transactions

JSON: {
"schedulingDate": "2024-03-13",
"amount": 2050.00
}

4 - Test 4

POST url:  localhost:9090/api/transactions

JSON: {
"schedulingDate": "2024-03-23",
"amount": 2050.00
}

5 - Test 5

POST url:  localhost:9090/api/transactions

JSON: {
"schedulingDate": "2025-03-23",
"amount": 2050.00
}

#
- Listing all transactions:


GET url:  localhost:9090/api/transactions

#
- Modifying a transaction

PUT url:  localhost:9090/api/transactions/1
JSON: {
"schedulingDate": "2025-03-23",
"amount": 2050.00
}

#
- Geting a transaction by Id

GET url:  localhost:9090/api/transactions/1

#
- Deleting a transaction by Id

DELETE url:  localhost:9090/api/transactions/1


