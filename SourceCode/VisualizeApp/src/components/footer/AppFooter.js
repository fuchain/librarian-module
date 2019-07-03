import React, { Component } from "react";
import "./AppFooter.css";

export default class AppFooter extends Component {
  render() {
    return (
      <div className="footer__copyright appfooter">
        <div>
          <a
            className="menu__link"
            rel="noopener noreferrer"
            target="_blank"
            href="https://library.fptu.tech"
          >
            © 2019 Bigchain.FPTu.tech
          </a>
        </div>
      </div>
    );
  }
}
