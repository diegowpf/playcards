import React, { Component } from 'react';
import PrimarySearchAppBar from './nav/primaryNavigation'
import Field from './field/field'
import Focus from './field/focusTabs'
import PlayCardMenu from './plays/playcardsmenu'
import './App.css';
import Grid from '@material-ui/core/Grid';
import Fab from '@material-ui/core/Fab';
import AddIcon from '@material-ui/icons/Add';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
// import primaryNavigation from "./nav/primaryNavigation";

class App extends Component {
  render() {
    return (
      <div className="App">
        <PrimarySearchAppBar/>
        <Focus/>
        <Grid container direction="row" justify="center">
          <Grid item xs ={11}>
            <br/>

          </Grid>
          <Grid  item xs={1}>
          <br/>
            <Fab color="primary" aria-label="Add">
              <AddIcon />
            </Fab>
          </Grid>
        </Grid>

        <Grid container>
          <Grid  justify="center" item xs={3}>
            <PlayCardMenu/>
          </Grid>
          <Grid item xs={7}>
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
