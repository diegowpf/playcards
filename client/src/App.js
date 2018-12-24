import React, { Component } from 'react';
import PrimarySearchAppBar from './nav/primaryNavigation'
import Field from './field/field'
import './App.css';
// import primaryNavigation from "./nav/primaryNavigation";

class App extends Component {
  render() {
    return (
      <div className="App">

        <PrimarySearchAppBar/>
        <Field/>
        <header className="App-header">
        </header>
      </div>
    );
  }
}

export default App;
