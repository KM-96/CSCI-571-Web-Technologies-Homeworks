import React from "react";
import AsyncSelect from "react-select/async";
import * as Constants from "../common/Constants";
import { withRouter } from "react-router-dom";
import debounce from "debounce-promise";

class SearchBar extends React.Component {
    constructor() {
        super();
        this.state = {
            isLoading: false,
            options: [],
            searchString: ""
        }
        this.loadOptions = debounce(this.loadOptions.bind(this), 1000);
    }


    loadOptions = async (inputValue) => {
        let url = Constants.BING_API + "?q=" + inputValue;
        const response = await fetch(
            url,
            {
              headers: {
                "Ocp-Apim-Subscription-Key": "28bb6aa85b734637b400611e40fcdd4f"
              }
            }
          );
          let data = await response.json()
          let options = data.suggestionGroups[0].searchSuggestions.map(
                    result => ({label: result.displayText}));
          return options;
    }


    render() {
        const searchBarCustomStyles = {
            option: (provided, state) => ({
                ...provided,
                backgroundColor: state.isFocused ? "#e6f2ff" : "white",
                color: "black"
            })
        }

        return (
            <AsyncSelect
                className="searchBar"
                placeholder={this.props.searchBar.placeholder}
                noOptionsMessage={() => this.props.searchBar.noOptionsMessage}
                loadOptions={this.loadOptions}
                onChange={this.props.action}
                value={this.props.search ? ({label: this.props.search}) : null}
                onInputChange={(inputValue) => this.props.handleInputChange(inputValue)}
                styles={searchBarCustomStyles}
            />
        )
    }
}

export default withRouter(SearchBar);