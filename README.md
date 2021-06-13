# ALG2 Semestrální práce

## 1. Zadání práce
* Cílem projektu bylo vytvořit zjednodušenou poštovní aplikaci, která by umožňovala podání a zpracování zásilek.
* Aplikace obsahuje přihlášení dle rolí - Customer a Admin.
* K ukládání dat je možné využívat buď soubory nebo DB.
* Jako externí knihovna byl použit JUnit.
## 2. Návrh řešení
### 2.1. Funkční specifikace
* Menu pro nepřihlášené:
  ````
    - Sledování zásilky
        - Výběr filtrování
    - Přihlášení
    - Registrace
    - O aplikaci
    - Ukončit
* Menu pro zákazníka:
  ````
    - Sledování zásilky
      - Výběr filtrování
    - Registrace zásilky
    - Odhlásit
* Menu pro admina:
  ````
    - Sklad
        - Sledování zásilky
            - Výběr filtrování
        - Odeslání transportu
        - Odhlásit
    - Pobočka pošty
        - Zásilky na skladě
        - Přijmutí zásilky
        - Odeslat transport do centrálního skladu
        - Roznést zásilky v našem PSČ
        - Odhlásit
### 2.2. Struktura souborů
Aplikace může běžet buď na datech ze souborů nebo z databáze. V této sekci si
představíme strukturu souborů. Soubory jsou uloženy v `data/storage/*.csv` ve formátu csv s oddělovačem `,`.

* **users.csv**

|Název              |Datový typ     |Popis                           |
|-------------------|---------------|--------------------------------|
|id                 |int            |ID uživatele                    |
|email              |string         |Email uživatele                 |
|password           |string         |Hashované heslo                 |
|firstname          |string         |Jméno                           |
|lastname           |string         |Přijmení                        |
|address_id         |int            |ID adresy bydliště              |
|created_at         |timestamp      |Datum a čas vytvoření           |
|enabled            |1/0            |Lze se přihlásit? (Nepoužito)   |
|role               |string         |Role - admin/customer           |

* **mails.csv**

|Název              |Datový typ     |Popis                           |
|-------------------|---------------|--------------------------------|
|id                 |int            |ID zásilky                      |
|sender_id          |int            |ID odesílatele                  |
|text_id            |string         |Textový ID zásilky              |
|location_id        |int            |ID pošty, kde se zásilka nachází|
|receiver_address_id|int            |ID adresy adresáta              |
|receiver_name      |string         |Jméno adresáta                  |
|status             |enum (viz níže)|Aktuální status zásilky         |
|type               |string         |Druh zásilky - balík, psaní     |
|info               |string         |Podtyp zásilky                  |
|last_changed_at    |timestamp      |Datum poslední změny            |

* **post_offices.csv**

|Název     |Datový typ|Popis          |
|----------|----------|---------------|
|id        |int       |ID pošty       |
|psc       |int       |PSČ            |
|address_id|int       |ID adresy pošty|

### 2.3. Diagram aplikace