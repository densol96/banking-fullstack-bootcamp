import styled from "styled-components";

const headingStyles = (fontSize, fontWeight) => {
  return `
    font-size: ${fontSize}rem;
    font-weight: ${fontWeight};
  `;
};

const Heading = styled.h1`
  ${(props) => props.as === "h1" && headingStyles(3.2, 600)}
  ${(props) => props.as === "h2" && headingStyles(2.4, 600)}
`;

export default Heading;
