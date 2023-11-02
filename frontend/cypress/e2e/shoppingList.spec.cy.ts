describe('Shopping List Page', () => {
    let shoppingListId;

    beforeEach(() => {
        // Krok 1: Zaloguj się na stronie
        cy.visit('http://localhost:3000/login');
        cy.get('input[name="email"]').type('test@test.pl');
        cy.get('input[name="password"]').type('test');
        cy.get('button').contains('Sign in').click();
        cy.url().should('eq', 'http://localhost:3000/');

        // Krok 2: Utwórz listę zakupów
        cy.get('button').contains('Create new').click();
        cy.get('input[name="name"]').type('Lista zakupów');
        cy.get('input[name="dateOfExecution"]').type('2023-11-15T00:00');
        cy.fixture('example.jpg').then(fileContent => {
            cy.get('input[name="attachmentImage"]').attachFile({
                fileContent,
                fileName: 'example.jpg',
                mimeType: 'image/jpeg',
            });
        });

        cy.get('p').contains('+').click();
        cy.get('input[name="product"]').type('Chleb');
        cy.get('input[name="quantity"]').type(2);
        cy.get('select').select('PIECE');
        cy.get('button').contains('Save').click();
        cy.get('.modal').should('not.exist');

        // Pobierz ID utworzonej listy zakupów
        cy.get('.list').last().invoke('attr', 'id').then(id => {
            shoppingListId = id;
        });
    });

    // afterEach(() => {
    //     cy.visit('http://localhost:3000');
    //
    //     cy.get('.list').last().contains('Lista zakupów')
    //         .should('exist')
    //         .click();
    //
    //     cy.get('button').contains('Delete').click();
    // })

    it('Powinna poprawnie wyświetlić stronę z pojedynczą listą zakupów', () => {
        // Krok 2: Kliknij w utworzoną listę zakupów
        cy.get('.list').last().contains('Lista zakupów').click();

        // Krok 3: Sprawdź czy poprawnie wyświetlają się informacje na stronie
        cy.get('nav').contains('Main page').should('exist');
        cy.get('nav').contains('Logout').should('exist');
        cy.contains('div', 'Name: Lista zakupów').should('exist');
        cy.contains('div', 'Status: Waiting').should('exist');
        cy.contains('div', 'Date of Execution: 15-11-2023 12:00:00').should('exist');
        cy.contains('div', 'Updated at:').should('exist');
        cy.contains('div', 'Created at:').should('exist');
        cy.get('img').should('exist');
        cy.get('button').contains('Edit').should('exist');
        cy.get('button').contains('Delete').should('exist');

        cy.get('.product-list').contains('Product').should('exist');
        cy.get('.product-list').contains('Quantity').should('exist');
        cy.get('.product-list').contains('Grammar').should('exist');
        cy.get('.product-list').contains('chleb')
        cy.get('.product-list').contains('2')
        cy.get('.product-list').contains('PIECE')

        // cleanup
        cy.visit('http://localhost:3000');
        cy.get('.list').last().contains('Lista zakupów')
            .should('exist')
            .click();

        cy.get('button').contains('Delete').click();
    });


    it('Powinna poprawnie usunąć liste zakupów', () => {
        cy.visit('http://localhost:3000');
        // Krok 2: Kliknij w utworzoną listę zakupów
        cy.get('.list').last().contains('Lista zakupów')
            .should('exist')
            .click();

        cy.get('button').contains('Delete').click();
        cy.url().should('eq', 'http://localhost:3000/');
        cy.get('.list').contains('Lista zakupów').should('not.exist');

    });

    it('Powinna umożliwić zaktualizowanie nazwy listy zakupów', () => {
        // Krok 4: Kliknij w utworzoną listę zakupów
        cy.get('.list').last().contains('Lista zakupów')
            .should('exist')
            .click();

        // Krok 5: Kliknij przycisk Edit i zaktualizuj dane
        cy.get('button').contains('Edit').click();
        cy.get('input[name="name"]').clear().type('Zaktualizowana Lista');
        cy.get('input[name="dateOfExecution"]').clear().type('2023-11-20T00:00');
        cy.get('input[name="completed"]').check();
        cy.fixture('example2.jpg').then(fileContent => {
            cy.get('input[name="attachmentImage"]').attachFile({
                fileContent: fileContent.toString(),
                fileName: 'updated-example.jpg',
                mimeType: 'image/jpeg',
            });
        });
        cy.get('button').contains('Save').click();
        cy.get('.modal').should('not.exist');

        // Krok 6: Sprawdź czy dane listy zakupów zostały zaktualizowane na stronie
        cy.contains('div', 'Name: Zaktualizowana Lista').should('exist');
        cy.contains('div', 'Status: Executed').should('exist');
        cy.contains('div', 'Date of Execution: 20-11-2023 12:00:00').should('exist');
        cy.get('img').should('exist');

        // clean up
        cy.visit('http://localhost:3000');
        cy.get('.list').last().contains('Zaktualizowana Lista')
            .should('exist')
            .click({force: true});

        cy.get('button').contains('Delete').click();
    });

    it('Powinna umożliwić zaktualizowanie  listy zakupów', () => {
        // Krok 4: Kliknij w utworzoną listę zakupów
        cy.get('.list').last().contains('Lista zakupów')
            .should('exist')
            .click();

        // Krok 5: Kliknij przycisk Edit i zaktualizuj dane
        cy.get('button').contains('Edit').click();

        cy.get('p').contains('+').click();
        cy.get('input[name="product"]').last().type('Maslo');
        cy.get('input[name="quantity"]').last().type(2);
        cy.get('select').last().select('KG', {force: true});

        cy.get('button').contains('Save').click();
        cy.get('.modal').should('not.exist');

        // Krok 6: Sprawdź czy dane listy zakupów zostały zaktualizowane na stronie
        cy.get('.product-list').contains('Product').should('exist');
        cy.get('.product-list').contains('Quantity').should('exist');
        cy.get('.product-list').contains('Grammar').should('exist');
        cy.get('.product-list').contains('chleb')
        cy.get('.product-list').contains('2')
        cy.get('.product-list').contains('PIECE')
        cy.get('.product-list').contains('maslo')
        cy.get('.product-list').contains('2')
        cy.get('.product-list').contains('KG')

        // clean up
        cy.visit('http://localhost:3000');
        cy.get('.list').last().contains('Lista zakupów')
            .should('exist')
            .click({force: true});

        cy.get('button').contains('Delete').click();
    });


    it('Powinna umożliwić usuniecie produktu z  listy zakupów', () => {
        // Krok 4: Kliknij w utworzoną listę zakupów
        cy.get('.list').last().contains('Lista zakupów')
            .should('exist')
            .click();

        // Krok 5: Kliknij przycisk Edit i zaktualizuj dane
        cy.get('button').contains('Edit').click();
        //
        // cy.get('p').contains('+').click();
        // cy.get('input[name="product"]').last().type('Maslo');
        // cy.get('input[name="quantity"]').last().type(2);
        // cy.get('select').last().select('G');
        //
        // cy.get('button').contains('Save').click();
        // cy.get('.modal').should('not.exist');



        cy.get("[data-testid='minus'").first().click();
        // cy.get('p').contains('-').l();
        cy.get('button').contains('Save').click();
        cy.get('.modal').should('not.exist');

        // Krok 6: Sprawdź czy dane listy zakupów zostały zaktualizowane na stronie
        cy.get('.product-list').contains('Product').should('exist');
        cy.get('.product-list').contains('Quantity').should('exist');
        cy.get('.product-list').contains('Grammar').should('exist');
        cy.get('.product-list').contains('chleb').should('not.exist')
        cy.get('.product-list').contains('2').should('not.exist')
        cy.get('.product-list').contains('PIECE').should('not.exist')

        // clean up
        cy.visit('http://localhost:3000');
        cy.get('.list').last().contains('Lista zakupów')
            .should('exist')
            .click({force: true});

        cy.get('button').contains('Delete').click();
    });

    it('Niepowodzenie aktualizacji  listy zakupów z niepoprawnymi danymi. - brak nazwy', () => {
        // Krok 1
        cy.get('.list').last().contains('Lista zakupów')
            .should('exist')
            .click();

        cy.get('button').contains('Edit').click();
        cy.get('.modal-wrapper').should('exist');

        // Krok 2
        cy.get('input[name="name"]').clear().type(' ');

        // Krok 3
        cy.get('button').contains('Save').click();


        // Krok 4
        cy.get('div').contains('name must not be blank')
        // clean up
        cy.visit('http://localhost:3000');
        cy.get('.list').last().contains('Lista zakupów')
            .should('exist')
            .click({force: true});

        cy.get('button').contains('Delete').click();

    });
    it('Niepowodzenie stworzenia nowej listy zakupów z niepoprawnymi danymi. - brak nazwy produktu', () => {
        // Krok 1
        cy.get('.list').last().contains('Lista zakupów')
            .should('exist')
            .click();

        cy.get('button').contains('Edit').click();
        cy.get('.modal-wrapper').should('exist');


        // Krok 3
        cy.get('p').contains('+').click();
        cy.get('input[name="product"]').first().clear().type(' ');
        cy.get('input[name="quantity"]').first().type(2);

        cy.get('button').contains('Save').click();

        // Krok 4
        cy.get('div').contains('products[0].name must not be blank')
        // clean up
        cy.visit('http://localhost:3000');
        cy.get('.list').last().contains('Lista zakupów')
            .should('exist')
            .click({force: true});

        cy.get('button').contains('Delete').click();

    });

    it('Niepowodzenie stworzenia nowej listy zakupów z niepoprawnymi danymi. - ujemna ilości produktu', () => {
        cy.get('.list').last().contains('Lista zakupów')
            .should('exist')
            .click();

        cy.get('button').contains('Edit').click();
        cy.get('.modal-wrapper').should('exist');
        // Krok 2

        // Krok 3
        cy.get('p').contains('+').click();
        cy.get('input[name="quantity"]').first().clear().type(-1);

        cy.get('button').contains('Save').click();

        // Krok 4
        cy.get('div').contains('products[0].quantity must be greater than or equal to 0')
        // clean up
        cy.visit('http://localhost:3000');
        cy.get('.list').last().contains('Lista zakupów')
            .should('exist')
            .click({force: true});

        cy.get('button').contains('Delete').click();
    });

});
