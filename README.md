# 📚 Sistema de Pesquisa de Funcionários – TP04 (LPR2)

Este projeto foi desenvolvido como parte da disciplina **Linguagem de Programação II (ADS471 – IFSP Cubatão)**.  
O objetivo principal é integrar uma aplicação Java com um banco de dados SQL Server utilizando JDBC, permitindo operações de pesquisa e navegação entre registros através de uma interface gráfica construída em Swing.

---

## 🎯 Objetivos do Trabalho (Conforme Enunciado)

- Desenvolver uma interface gráfica em Java (Swing) semelhante ao modelo apresentado em sala.
- Criar um banco de dados no SQL Server chamado **aulajava**, contendo as tabelas:
  - **tbcargos**
  - **tbfuncs**
- Inserir registros respeitando chaves primárias, estrangeiras e relacionamentos.
- Estabelecer conexão com o banco via **JDBC** (Driver SQL Server).
- Implementar funcionalidade de pesquisa utilizando:
  - `SELECT ... LIKE ?`
  - `PreparedStatement`
  - Preenchimento de um `ResultSet`.
- Implementar navegação pelos registros retornados através dos botões:
  - **Anterior**
  - **Próximo**
- Garantir que a navegação respeite os limites do ResultSet (primeiro e último registro).

---

## 🛠️ Tecnologias Utilizadas

- **Java 8+**
- **JDBC — SQL Server**
- **SQL Server 2017+**
- **Swing / AWT**
- **VS Code**
- **Git e GitHub**

---

## 🗄️ Modelagem do Banco de Dados – *aulajava*

### 📌 Tabela **tbcargos**
| Campo     | Tipo         | Null | Descrição                |
|-----------|--------------|------|--------------------------|
| cd_cargo  | INT (PK)     | NÃO  | Identificador do cargo   |
| ds_cargo  | VARCHAR(30)  | NÃO  | Descrição do cargo       |

### 📌 Tabela **tbfuncs**
| Campo      | Tipo           | Null | Descrição                                      |
|------------|----------------|------|------------------------------------------------|
| cod_func   | INT (PK)       | NÃO  | Código do funcionário                          |
| nome_func  | VARCHAR(50)    | NÃO  | Nome do funcionário                            |
| sal_func   | DECIMAL(10,2)  | SIM  | Salário                                        |
| cod_cargo  | INT (FK)       | SIM  | Relacionamento com `tbcargos(cd_cargo)`        |

---

## 📥 Registros Inseridos

O programa insere automaticamente os seguintes colaboradores e cargos (caso ainda não existam no banco):

### 👔 Cargos
- Gerente de TI  
- Estagiário  

### 👥 Funcionários
- **Tuler** – Gerente de TI  
- **Matheus** – Estagiário  
- **Davi** – Estagiário  

---

## 📁 Estrutura do Projeto

```
TP_04-LP2/
├── lib/
│   └── mssql-jdbc-8.4.1.jre8.jar
├── src/
│   └── Form.java
├── .gitignore
└── README.md
```

---

## 🚀 Como Executar o Projeto

### 1️⃣ Clonar o repositório
```bash
git clone https://github.com/mattfrench2024/CBTLPR2_TP_04
```

### 2️⃣ Acessar a pasta
```bash
cd CBTLPR2_TP_04
```

### 3️⃣ Compilar o programa
```bash
javac -encoding UTF-8 -cp ".;lib/mssql-jdbc-8.4.1.jre8.jar" src/Form.java -d .
```

### 4️⃣ Executar o programa
```bash
java -cp ".;lib/mssql-jdbc-8.4.1.jre8.jar;." Form
```

---

## 🖥️ Funcionalidades Implementadas

### 🔍 Pesquisa
- Busca funcionários pelo nome utilizando `LIKE ?`
- Uso de `PreparedStatement` para evitar SQL Injection
- Preenchimento automático dos campos ao retornar o ResultSet

### 🔄 Navegação
- Botão **Próximo** → avança um registro  
- Botão **Anterior** → retorna um registro  
- Travamento automático no primeiro e no último registro  

### 🧾 Exibição dos dados
- Nome  
- Salário  
- Cargo  

---

## 👨‍🏫 Autores

**Matheus Correia de França**  

**Davi Leite Coelho**  

---

## 📚 Referências

- Documentação oficial do Java  
  https://docs.oracle.com/en/java/

- Driver JDBC SQL Server  
  https://learn.microsoft.com/pt-br/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server
