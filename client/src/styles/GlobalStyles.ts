import { createGlobalStyle } from "styled-components";

const GlobalStyles = createGlobalStyle`:root {
  --color-primary: #5ec576;
  --color-secondary: #ffcb03;
  --color-tertiary: #ff585f;
  --color-primary-darker: #4bbb7d;
  --color-secondary-darker: #ffbb00;
  --color-tertiary-darker: #fd424b;
  --color-primary-opacity: #5ec5763a;
  --color-secondary-opacity: #ffcd0331;
  --color-tertiary-opacity: #ff58602d;
  --gradient-primary: linear-gradient(to top left, #39b385, #9be15d);
  --gradient-secondary: linear-gradient(to top left, #ffb003, #ffcb03);
  --gradient-tertiary: linear-gradient(to top left, #fd424b, #ff58602d);
}

* {
  margin: 0;
  padding: 0;
  box-sizing: inherit;
}

html {
  font-size: 62.5%;
  box-sizing: border-box;
}

body {
  font-family: 'Poppins', sans-serif;
  font-weight: 300;
  color: #444;
  line-height: 1.9;
  background-color: #f3f3f3;
  font-size: 1.6rem;
}

input,
button,
textarea,
select {
  font-family: inherit;
  color: inherit;
}

button {
  cursor: pointer;
  border: none;
  background: transparent;
}

*:disabled {
  cursor: not-allowed;
}

a {
  color: inherit;
  text-decoration: none;
}

ul {
  list-style: none;
}

img {
  max-width: 100%;
}

.container {
    max-width: 115rem;
    width: 100%;
}


`;

export default GlobalStyles;
