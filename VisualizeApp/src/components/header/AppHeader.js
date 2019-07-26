import React, { Component } from "react";
import { Header, Item } from "semantic-ui-react";
import "./AppHeader.css";

export default class AppHeader extends Component {
  render() {
    return (
      <Header id="appHeader" className="App-header" size="small">
        <Item className="logo-item">
          <img alt="logo" src="/img/logo.png" style={{ maxWidth: "50px" }} />
        </Item>
      </Header>
    );
  }
}
