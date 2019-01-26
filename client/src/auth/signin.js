//https://p.w3layouts.com/demos/rugby/web/images/ba2.jpg
import React from 'react';
import LazyHero from 'react-lazy-hero'

class SignIn extends React.Component{
  state = {}

//https://images.unsplash.com/photo-1542693209-f2b4ed97f70d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2680&q=80
//https://images.unsplash.com/photo-1508355991726-ebd81e4802f7?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2706&q=80
  render() {
    return(
      <div>
        <LazyHero minHeight="95vh"
          opacity = ".25"
          imageSrc="https://images.unsplash.com/photo-1542693209-f2b4ed97f70d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2680&q=80">

            <h1>Generic Startup Hype Headline</h1>
        </LazyHero>
    </div>);
  }
}

export default SignIn;
