# 2. Use Material UI

Date: 2025-07-12

## Status

Accepted

## Context

A need to establish a consistent and modern styling for the web application.
Considered solutions:
* Tailwind
* Bootstrap
* Material UI

## Decision

Decision to use Material UI React component library. This project may not benefit
from the customizability offered by Tailwind and Bootstrap is simply outdated.

## Consequences

It may become harder to introduce custom functionalities that are consistent
with Material UI if such need arises. Material UI is also said to have some
performance overhead compared to Tailwind but the impact hasn't been evaluated.
