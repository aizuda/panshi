export function random(number: number = 6) {
  return Math.floor(10 * number + Math.random() * 900000);
}
