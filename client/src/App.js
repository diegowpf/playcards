import React, { Component } from 'react';
import PrimarySearchAppBar from './nav/primaryNavigation'
import Field from './field/field'
import Focus from './field/focus'
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
        <Grid container direction="row" justify="center">
          <Grid item xs ={11}>
            <br/>
            <Focus/>
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
          <Card>
            <CardContent>
              <Typography color="textSecondary" gutterBottom>
              </Typography>
              <Typography variant="h5" component="h2">
                OFFENSE
              </Typography>
              <Typography color="textSecondary">
                Offense Plays
              </Typography>
              <Typography component="p">
                Design offensive plays
                <br />
              </Typography>
            </CardContent>
          </Card>
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
