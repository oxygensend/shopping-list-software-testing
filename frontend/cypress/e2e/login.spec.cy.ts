describe("Login page", () => {
    it('Powinien poprawnie wyświetlić strone logowania', () => {

        // Krok 1
        cy.visit('http://localhost:3000')
        cy.url().should('include', '/login')

        // Krok 2
        cy.get('label').should('contain', 'Email');
        cy.get('input[type="email"]').should('exist');
        cy.get('label').should('contain', 'Password');
        cy.get('input[type="password"]').should('exist');
        cy.get('button').should('contain', 'Sign in').should('exist');

        // Krok 3
        cy.contains('div', 'Error').should('not.exist');

    })

    it('Powinien zalogować użytkownika do systemu', () => {
        // Krok 1
        cy.visit('http://localhost:3000');
        cy.url().should('include', '/login');

        // Krok 2
        cy.get('input[type="email"]').type('test@test.com');
        cy.get('input[type="password"]').type('test');

        // Krok 3
        cy.get('button').contains('Sign in').click();
        cy.url().should('eq', 'http://localhost:3000/');

        // Krok 4
        cy.url().should('eq', 'http://localhost:3000/');
    });

    it('Powinien wyświetlić komunikat o niepowodzeniu logowania', () => {
        // Krok 1
        cy.visit('http://localhost:3000');
        cy.url().should('include', '/login');

        // Krok 2
        cy.get('input[type="email"]').type('incorrect@example.com');
        cy.get('input[type="password"]').type('incorrectpassword');

        // Krok 3
        cy.get('button').contains('Sign in').click();

        // Krok 4
        cy.contains('div', 'Bad credentials').should('exist');
        cy.url().should('include', '/login');
    });
})
