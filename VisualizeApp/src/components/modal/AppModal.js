import React, { Component } from "react";
import { Modal } from "semantic-ui-react";
import ReactJson from "react-json-view";
import "./AppModal.css";
class AppModal extends Component {
  open = false;
  close = () => {
    this.props.onClose();
  };
  render() {
    return (
      <div>
        <Modal
          dimmer={true}
          open={this.props.state.open}
          onClose={this.close}
          closeIcon
        >
          <Modal.Header>
            <span style={{ color: "#7367F0" }}>{this.props.state.title}</span>
          </Modal.Header>
          <Modal.Content>
            <Modal.Description>
              <div style={{ padding: "1rem" }}>
                <ReactJson src={this.props.state.content} />
              </div>
            </Modal.Description>
          </Modal.Content>
        </Modal>
      </div>
    );
  }
}

export default AppModal;
