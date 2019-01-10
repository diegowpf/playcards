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

function PlayCardMenu(props) {
  const { classes } = props;
  const bull = <span className={classes.bullet}>•</span>;

  return (
    <div>
    <Paper className={classes.root} elevation={1}>
      <InputBase className={classes.input} placeholder="Search PlayCards" />
      <IconButton className={classes.iconButton} aria-label="Search">
        <SearchIcon />
      </IconButton>
      <Divider className={classes.divider} />
    </Paper>
    <br/>
    <Card className={classes.card}>
      <CardContent>
        <Typography variant="h5" component="h2">
          Danger Right
        </Typography>
        <Typography className={classes.pos} color="textSecondary">
          Standard Layout
        </Typography>
        <Typography component="p">
          Running Back Wide
        </Typography>
      </CardContent>
    </Card>
    </div>
  );
}

PlayCardMenu.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(PlayCardMenu);
