package helper

import play.api.libs.iteratee.{Done, Cont, Input, Iteratee}

object Iteratees {

  def contains(string: Array[Byte]): Iteratee[Array[Byte], Boolean] = {
    def step(k: Int): Input[Array[Byte]] => Iteratee[Array[Byte], Boolean] = {
      case Input.El(bytes) =>
        @scala.annotation.tailrec
        def cursor(i: Int, k: Int): Option[Int] =
          if (k == string.length) None
          else if (i == bytes.length) Some(k)
          else if (bytes(i) == string(k)) cursor(i + 1, k + 1)
          else cursor(i + 1, 0)
        cursor(0, k).map(k => Cont(step(k))).getOrElse(Done(true, Input.Empty))
      case Input.Empty => Cont(step(k))
      case Input.EOF => Done(false, Input.EOF)
    }
    Cont(step(0))
  }

}
