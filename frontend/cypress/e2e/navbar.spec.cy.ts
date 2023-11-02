describe("Navbar", () => {
    beforeEach(() => {
        // Krok 1: Zaloguj się na stronie
        cy.visit('http://localhost:3000/login');
        cy.get('input[name="email"]').type('test@test.pl');
        cy.get('input[name="password"]').type('test');
        cy.get('button').contains('Sign in').click();
        cy.url().should('eq', 'http://localhost:3000/');


    });
    it ("Powinno przekierować na strone główną", () => {
        cy.get('nav').contains('Main page').click();
        cy.url().should('eq', 'http://localhost:3000/');
    })
    it ("Powinno wylogować użytkownika", () => {
        cy.get('nav').contains('Logout').click();
        cy.url().should('eq', 'http://localhost:3000/login');
    })


})