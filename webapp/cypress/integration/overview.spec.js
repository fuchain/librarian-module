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
    cy.get(".vs-switch").click();
    cy.contains("Cập nhật thông tin").click();
    cy.contains("Sách đang giữ").click({ force: true });
    cy.contains("Làm mới").click();
    cy.contains("Trang chính").click({ force: true });
  });

  it("Book Keeping Page", function() {
    cy.login();
    cy.visit("/books/keeping");
    cy.contains("Sách đang giữ");
    cy.contains("Làm mới").click();
  });

  it("Book Returning Page", function() {
    cy.login();
    cy.visit("/books/returning");
    cy.contains("Sách đang trả");
    cy.contains("Làm mới").click();
  });

  it("Book Requesting Page", function() {
    cy.login();
    cy.visit("/books/requesting");
    cy.contains("Sách yêu cầu");
    cy.contains("Làm mới").click();
  });

  it("Book Request Page", function() {
    cy.login();
    cy.visit("/books/request");
    cy.get("#book-search").type("Javascript");
    cy.contains("Tìm sách").click();
    cy.contains("Building javascript");
  });

  it("Transfer History Page", function() {
    cy.login();
    cy.visit("/transfer-history");
    cy.contains("Lịch sử nhận sách");
  });

  it("Review Page", function() {
    cy.login();
    cy.visit("/review-ux");
    cy.contains("Đánh giá trải nghiệm");
  });
});
