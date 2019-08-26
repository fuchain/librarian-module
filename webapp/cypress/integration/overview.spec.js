/* eslint-disable no-undef */
describe("Overview Test", function() {
  it("Login", function() {
    cy.login();
  });

  it("Home Page", function() {
    cy.login();
    cy.visit("/");
    cy.contains("Sử dụng App để mượn trả sách thư viện?");
  });

  it("Profile Page", function() {
    cy.login();
    cy.visit("/profile");
    cy.contains("Hồ sơ");
    cy.contains("Sách đang giữ").click({ force: true });
    cy.contains("Trang chính").click({ force: true });
  });

  it("Book Keeping Page", function() {
    cy.login();
    cy.visit("/books/keeping");
    cy.contains("Sách đang giữ");
  });

  it("Book Returning Page", function() {
    cy.login();
    cy.visit("/books/returning");
    cy.contains("Sách đang trả");
  });

  it("Book Requesting Page", function() {
    cy.login();
    cy.visit("/books/requesting");
    cy.contains("Sách yêu cầu");
  });

  it("Book Request Page", function() {
    cy.login();
    cy.visit("/books/request");
    cy.get("#book-search").type("Javascript");
  });

  it("Transfer History Page", function() {
    cy.login();
    cy.visit("/transfer-history");
    cy.contains("Lịch sử nhận sách");
  });
});
