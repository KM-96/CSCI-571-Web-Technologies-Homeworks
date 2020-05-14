import React from "react";
import Switch from "react-switch";
import * as Constants from "../common/Constants";
import { withRouter } from "react-router-dom";

class ToggleSwitch extends React.Component {

    constructor() {
        super();
        this.state = {
            isDisabled: false,
            checked: true
        };
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange() {
        if(this.state.isDisabled) {
            return;
        }
        this.setState ({ isDisabled: true});
        this.resetGuardianFlagInLocalStorage();
        this.setState(previousState => {
            return {
                checked: !previousState.checked
            }
        });
        this.props.history.push("/articles/" + this.props.section);
        this.setState({isDisabled: false});
    }

    componentDidMount() {
        let flag = window.localStorage.getItem(Constants.IS_GUARDIAN_KEY);
        if(flag === null || flag === undefined) {
            window.localStorage.setItem(Constants.IS_GUARDIAN_KEY , "true");
        } else {
            let isChecked = (flag === "true") ? true: false;
            this.setState({
                isDisabled: false,
                checked: isChecked
            });
        }
    }

    resetGuardianFlagInLocalStorage() {
        let flag = window.localStorage.getItem(Constants.IS_GUARDIAN_KEY);
        flag = (flag === "true") ? "false" : "true";
        window.localStorage.setItem(Constants.IS_GUARDIAN_KEY , flag);
    }

    render() {
        return (
            this.state.isDisabled ?
            <Switch
                onChange={this.handleChange}
                checked={this.state.checked}
                height={21}
                width={42}
                className="react-switch"
                id="material-switch"
                disabled
            />
            :
            <Switch
                onChange={this.handleChange}
                checked={this.state.checked}
                onColor="#1E90FF"
                offColor="#D3D3D3"
                uncheckedIcon={false}
                checkedIcon={false}
                height={21}
                width={42}
                className="react-switch"
                id="material-switch"
            />
        )
    }
}

export default withRouter(ToggleSwitch);