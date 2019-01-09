import React, { Component } from 'react';
import PrimarySearchAppBar from './nav/primaryNavigation'
import Field from './field/field'
import './App.css';
import Grid from '@material-ui/core/Grid';
import Fab from '@material-ui/core/Fab';
import AddIcon from '@material-ui/icons/Add';
// import primaryNavigation from "./nav/primaryNavigation";

class App extends Component {
  render() {
    return (
      <div className="App">

        <PrimarySearchAppBar/>
        <Grid container direction="row" justify="flex-end">
          <Grid item xs ={11}>
          </Grid>
          <Grid  item xs={1}>
          <br/>
            <Fab color="primary" aria-label="Add">
              <AddIcon />
            </Fab>
          </Grid>
        </Grid>

        <Grid container spacing={24}>
          <Grid item xs={8}>
          </Grid>
          <Grid item xs={4}>
            <Field/>
          </Grid>
        </Grid>

        <header className="App-header">
        </header>
      </div>
    );
  }
}

export default App;
