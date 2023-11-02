describe('ShoppingLists Page', () => {
    beforeEach(() => {
        // Krok 1
        cy.visit('http://localhost:3000/login');
        cy.get('input[name="email"]').type('test@test.pl');
        cy.get('input[name="password"]').type('test');
        cy.get('button').contains('Sign in').click();
        cy.url().should('eq', 'http://localhost:3000/');
    });

    afterEach(() => {

    })

    it('Powinna wyświetlić się strona główna z listami zakupów', () => {
        // Krok 2
        cy.contains('p', 'Collection of shopping lists').should('exist');
        cy.get('nav').contains('Logout').should('exist');
        cy.get('nav').contains('Main page').should('exist');
        cy.get('button').contains('Create new').should('exist');
        cy.get('.list').should('exist');
    });

    it('Powinna umożliwić stworzenie nowej listy zakupów', () => {
        // Krok 2

        cy.get('button').contains('Create new').click();
        cy.get('.modal-wrapper').should('exist');

        // Krok 3
        cy.get('input[name="name"]').type('Lista zakupów na weekend');
        cy.get('input[name="dateOfExecution"]').type('2023-11-10T08:00');

        cy.fixture('example.jpg').then(fileContent => {
            cy.get('input[type="file"]').attachFile({fileContent, fileName: 'example.jpg', mimeType: 'image/jpeg'});
        });
        cy.get('p').contains('+').click();
        cy.get('input[name="product"]').type('Chleb');
        cy.get('input[name="quantity"]').type(2);
        cy.get('select').select('PIECE');

        // Krok 4
        cy.get('button').contains('Save').click();
        cy.get('.modal').should('not.exist');

        // Krok 5
        cy.get('.list').last().contains('Lista zakupów na weekend')
            .should('exist')
            .click();

        // Krok 6
        cy.get('p').contains('Lista zakupów na weekend')
        cy.get('p').contains('Waiting')
        cy.get('p').contains('10-11-2023 08:00:00')
        cy.get('.product-list').contains('chleb')
        cy.get('.product-list').contains('2')
        cy.get('.product-list').contains('PIECE')

        cy.get('button').contains('Delete').click()

    });

    it('Niepowodzenie stworzenia nowej listy zakupów z niepoprawnymi danymi. - brak nazwy', () => {

        // Krok 2
        cy.get('button').contains('Create new').click();
        cy.get('.modal-wrapper').should('exist');

        // Krok 3
        cy.get('button').contains('Save').click();


        // Krok 4
        cy.get('input:invalid').should('have.length', 1)

    });
    it('Niepowodzenie stworzenia nowej listy zakupów z niepoprawnymi danymi. - brak nazwy produktu', () => {

        // Krok 2
        cy.get('button').contains('Create new').click();
        cy.get('.modal-wrapper').should('exist');

        // Krok 3
        cy.get('input[name="name"]').type('Lista zakupów');

        cy.get('p').contains('+').click();
        cy.get('input[name="quantity"]').type(2);
        cy.get('select').select('PIECE');

        cy.get('button').contains('Save').click();

        // Krok 4
        cy.get('div').contains('products[0].name must not be blank')


    });

    it('Niepowodzenie stworzenia nowej listy zakupów z niepoprawnymi danymi. - ujemna ilości produktu', () => {

        // Krok 2
        cy.get('button').contains('Create new').click();
        cy.get('.modal-wrapper').should('exist');

        // Krok 3
        cy.get('input[name="name"]').type('Lista zakupów');
        cy.get('p').contains('+').click();
        cy.get('input[name="product"]').type("test");
        cy.get('input[name="quantity"]').clear().type(-1);
        cy.get('select').select('PIECE');

        cy.get('button').contains('Save').click();

        // Krok 4
        cy.get('div').contains('products[0].quantity must be greater than or equal to 0')

    });

    it('Niepowodzenie stworzenia nowej listy zakupów z niepoprawnymi danymi. - brak produktu', () => {

        // Krok 2
        cy.get('button').contains('Create new').click();
        cy.get('.modal-wrapper').should('exist');

        // Krok 3
        cy.get('input[name="name"]').type('Lista zakupów');
        cy.get('button').contains('Save').click();

        // Krok 4
        cy.get('div').contains('products must not be empty')

    })
});
