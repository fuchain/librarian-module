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
    "KDeJKo7BhPRCsVwmBjnmuceeFwg1jE6zuLoRnkXy3bL"
  );
  window.localStorage.setItem(
    "fptlibrarian_privateKey",
    "DKDeMCSqxPQ6vgqddGPzpWPmVvnP61YkU1v77Wwkm8t9"
  );
  window.localStorage.setItem(
    "fptlibrarian_access_token",
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InR1aG1zZTYyNTMxQGZwdC5lZHUudm4iLCJyb2xlcyI6InJlYWRlciIsImlhdCI6MTU2NDQxMDMwMSwiZXhwIjoxNTY0NDk2NzAxfQ.BFcDQEuPQLuQ7i0AS4dIjE6jYzps2-619BMqoOkBkHc"
  );
});
