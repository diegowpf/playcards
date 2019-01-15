import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
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
import * as d3 from "d3";
import axios from 'axios'

const styles = {
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
};

var playCards = [];

export default class PersonList extends React.Component{

    // const { classes } = props;
    // const bull = <span className={classes.bullet}>•</span>;
  state = {
      playCards: [{name: "Danger Right", formation: "Standard Layout", description: "Running Back Wide"},
                  {name: "Some New Play", formation: "Standard Layout", description: "Three Wide Recievers"},
                  {name: "True Detective", formation: "Standard Layout", description: "Halfback"}]
  }

  componentDidMount(){
    axios.get("http://server.immersivesports.ai/playcards/team/c679919f-d524-3f75-ad2a-5161706e12a5")
      .then(res => {
        playCards = res.data;
        console.log(JSON.stringify(playCards));

        //this.setState({playCards});
      })
  }

  render(){
    return (
      <div id="availableCards">
       { this.state.playCards.map(card =>
         <div>
           <Card>
            <CardContent>
            <Typography variant="h5" component="h2">
              {card.name}
            </Typography>
            <Typography  color="textSecondary">
              {card.formation}
            </Typography>
            <Typography component="p">
              {card.description}
            </Typography>
            </CardContent>
           </Card>
           <br/>
         </div>
     )}
      </div>
    );
  }
}

// PlayCardMenu.propTypes = {
//   classes: PropTypes.object.isRequired,
// };

// export default withStyles(styles)(PlayCardMenu);
