# Sistema de Eventos (Console Java)

## Sobre o projeto
Este projeto foi desenvolvido como trabalho de faculdade para a unidade curricular **Programação de Soluções Computacionais**.

Este repositório contém a solução da atividade **Pratique** disponibilizada na matéria.

- **Universidade:** Universidade Anhembi Morumbi
- **Curso:** Sistemas de Informação
- **Unidade Curricular:** Programação de Soluções Computacionais
- **Semestre:** 1

## Aluno
- **Nome:** Vinícius Baggio
- **LinkedIn:** https://www.linkedin.com/in/vbaggio/
- **E-mail:** vnc.baggio@gmail.com

## Enunciado
O enunciado da atividade está disponível em:
- [docs/pratique.pdf](docs/pratique.pdf)

## Objetivo
Implementar um sistema em Java, executado em console, para cadastro e consulta de eventos, incluindo confirmação e cancelamento de participação, com persistência em arquivos texto.

## Funcionalidades implementadas
- Cadastro de usuário
- Listagem de usuários
- Cadastro de evento (nome, cidade, endereço, categoria, data/hora, duração e descrição)
- Listagem de todos os eventos ordenados por data/hora
- Confirmação de participação em evento
- Cancelamento de participação
- Listagem de eventos confirmados por usuário
- Listagem de eventos já ocorridos
- Listagem de eventos na cidade do usuário
- Cálculo de status do evento: `UPCOMING`, `ONGOING`, `PAST`
- Validações de domínio:
  - Não permite e-mail de usuário duplicado
  - Não permite cadastrar evento com data/hora no passado

## Persistência de dados
Os dados são persistidos em arquivos `.data` dentro da pasta `data/`:

- `data/users.data`
- `data/events.data`
- `data/participations.data`

Os arquivos são carregados ao iniciar a aplicação e gravados ao encerrar.

## Estrutura do projeto
Código-fonte principal em:
- `src/main/java/com/pratique/psc/s1/`

Organização em camadas:
- `controller` (interação de console)
- `service` (regras de negócio)
- `repository` e `repository/impl/file` (acesso e persistência)
- `domain/entity` e `domain/enums` (modelo de domínio)

## Requisitos e execução
- Java 21

Compilação e execução podem ser feitas por IDE (com Java 21) ou via terminal na raiz do projeto.
