import React, { Component } from 'react';
import PrimarySearchAppBar from './nav/primaryNavigation'
import Field from './field/field'
import Focus from './field/focusTabs'
import PlayCardMenu from './plays/playcardsmenu'
import './App.css';
import Grid from '@material-ui/core/Grid';
import Fab from '@material-ui/core/Fab';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import InputBase from '@material-ui/core/InputBase';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import SearchIcon from '@material-ui/icons/Search';
import DirectionsIcon from '@material-ui/icons/Directions';
import {BrowserRouter, Route} from 'react-router-dom';
import SignIn from './auth/signin'
import { withStyles } from '@material-ui/core/styles';
import axios from 'axios';
import Confirmation from './upload/confirmation';
import DocumentUploader from './upload/documentuploader'
// import primaryNavigation from "./nav/primaryNavigation";
const styles = theme => ({
  card: {
    minWidth: 275,
  },
  bullet: {
    display: 'inline-block',
    margin: '0 2px',
    transform: 'scale(0.8)',
  },
  title: {
    fontSize: 14,
  },
  pos: {
    marginBottom: 12,
  },
  button: {
  margin: theme.spacing.unit,
  },
  input: {
    display: 'none',
  },
});

class App extends Component {

  classes = {};

  constructor(props){
    super();
    this.classes = props.classes;
  }



  render() {

    return (
      <div>
      <PrimarySearchAppBar/>
      <BrowserRouter>
        <div>
          <Route exact={true} path='/' render={() => (
            <div className="App">

              <Focus/>
              <Grid container direction="row" justify-items="space-around">
                <Grid item xs ={11}>
                  <br/>
                </Grid>
                <Grid  item xs={1}>
                <br/>
                  <DocumentUploader />
                  <Confirmation />
                </Grid>
              </Grid>
              <Grid container spacing={0} justify="space-around">
                <Grid item xs={3}>
                  <PlayCardMenu/>
                </Grid>
                <Grid item xs={8}>
                  <Field/>
                </Grid>
              </Grid>
              <header className="App-header">
              </header>
            </div>
          )}/>
          <Route exact={true} path='/signin' render={() => (
            <div className="App">
              <SignIn/>
            </div>
          )}/>
        </div>
      </BrowserRouter>
      </div>
    );
  }
}

export default withStyles(styles)(App);
