describe("Login page" , () => {
    it('render login page properly', () => {

        cy.visit('http://localhost:3000')
        cy.url().should('include', '/login')

        cy.get("['data-testid=login-form']").should('be.visible')
        cy.get('label').should('contain', 'Email:');
        cy.get('input[type="email"]').should('exist');
        cy.get('label').should('contain', 'Password:');
        cy.get('input[type="password"]').should('exist');
        cy.get('button').should('contain', 'Sign in').should('exist');


    })

})