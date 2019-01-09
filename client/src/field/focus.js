import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import Grid from '@material-ui/core/Grid';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';

const styles = {
  card: {
    minWidth: 275,
    maxWidth: 400
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

function Focus(props) {
  const { classes } = props;
  const bull = <span className={classes.bullet}>•</span>;

  return (
    <Grid container xs={24} justify="center" spacing={16}>
      <Grid item >
      <Card className={classes.card}>
        <CardContent>
          <Typography className={classes.title} color="textSecondary" gutterBottom>
          </Typography>
          <Typography variant="h5" component="h2">
            OFFENSE
          </Typography>
          <Typography className={classes.pos} color="textSecondary">
            Offense Plays
          </Typography>
          <Typography component="p">
            Design offensive plays
            <br />

          </Typography>
        </CardContent>
      </Card>
      </Grid>
      <Grid item>
      <Card className={classes.card}>
        <CardContent>
          <Typography className={classes.title} color="textSecondary" gutterBottom>
          </Typography>
          <Typography variant="h5" component="h2">
            DEFENSE
          </Typography>
          <Typography className={classes.pos} color="textSecondary">
            Defense Plays
          </Typography>
          <Typography component="p">
            Design defensive plays
            <br />
          </Typography>
        </CardContent>
      </Card>
      </Grid>
    </Grid>
  );
}

Focus.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(Focus);
