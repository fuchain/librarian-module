/* eslint-disable no-undef */
// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This is will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })

Cypress.Commands.add("login", () => {
  window.localStorage.setItem(
    "fptlibrarian_publicKey",
    "8bcb0d1b2218f92042c28b5713eb3bb3003cb9841505c92c900f4af786f1bca610c254f503eaa7fc18f6fb92ea8d59c0"
  );
  window.localStorage.setItem(
    "fptlibrarian_privateKey",
    "404026ef05ac97587eef5b82bdd058322ee581e8c21c359e7cdd4569ecba20d34412e707c6670c9837f7db6dcdce1589"
  );
  window.localStorage.setItem(
    "fptlibrarian_access_token",
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InR1aG1zZTYyNTMxQGZwdC5lZHUudm4iLCJyb2xlcyI6InJlYWRlciIsImlhdCI6MTU2Njg0NDA2OCwiZXhwIjoxNjY2OTMwNDY4fQ.OB9n206jQu5QZlGnuygVNdxgdSQsk7lOfN_6KnLXDg8"
  );
});
