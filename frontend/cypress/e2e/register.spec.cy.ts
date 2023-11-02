
describe('Register Page', () => {
    it('Powinna wyświetlić się strona rejestracji w aplikacji', () => {
        // Krok 1
        cy.visit('http://localhost:3000/register');
        cy.url().should('include', '/register');

        // Krok 2
        cy.get('label').should('contain', 'Email');
        cy.get('input[name="email"]').should('exist');
        cy.get('label').should('contain', 'Firstname');
        cy.get('input[name="firstName"]').should('exist');
        cy.get('label').should('contain', 'Lastname');
        cy.get('input[name="lastName"]').should('exist');
        cy.get('label').should('contain', 'Password');
        cy.get('input[name="password"]').should('exist');
        cy.get('button').should('contain', 'Sign up').should('exist');

        // Krok 3
        cy.contains('div', 'Error').should('not.exist');
    });

    it('Powinna umożliwić zarejestrowanie użytkownika w systemie', () => {

        const email =  Math.random().toString(36).substring(2,11) + '@example.com';
        // Krok 1
        cy.visit('http://localhost:3000/register');
        cy.url().should('include', '/register');

        // Krok 2
        cy.get('input[name="firstName"]').type('Jan');
        cy.get('input[name="lastName"]').type('Kowalski');
        cy.get('input[name="email"]').type(email);
        cy.get('input[name="password"]').type('haslo123');

        // Krok 3
        cy.get('button').contains('Sign up').click();
        cy.url().should('eq', 'http://localhost:3000/');
    });

    it('Nie powinna zarejestrować użytkownika z niepoprawnymi danymi', () => {
        // Krok 1
        cy.visit('http://localhost:3000/register');
        cy.url().should('include', '/register');

        // Krok 2
        cy.get('input[name="firstName"]').type('J');
        cy.get('input[name="lastName"]').type('K');
        cy.get('input[name="email"]').type('jan.kowalski@test.com');
        cy.get('input[name="password"]').type('abc');

        // Krok 3
        cy.get('button').contains('Sign up').click();

        // Sprawdzanie komunikatów o błędach
        cy.contains('div', 'firstName size must be between 2 and 64').should('exist');
        cy.contains('div', 'lastName size must be between 2 and 64').should('exist');
        cy.contains('div', 'password size must be between 4 and 64').should('exist');
    });

});
