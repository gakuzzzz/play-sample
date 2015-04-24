package helper

import play.api.libs.iteratee.{Done, Cont, Input, Iteratee}

object Iteratees {

  def contains(string: Array[Byte]): Iteratee[Array[Byte], Boolean] = {
    @scala.annotation.tailrec
    def contains(buffers: Iterator[Array[Byte]]): Iteratee[Array[Byte], Boolean] =	{
      val buffer = buffers.next()
      if (buffer.sameElements(string))
        Done(true, Input.Empty)
      else if (buffers.hasNext)
        contains(buffers)
      else
        Cont(step(buffer))
    }
    def step(buffer: Array[Byte]): Input[Array[Byte]] => Iteratee[Array[Byte], Boolean] = {
      case Input.El(input) =>
        val bytes = buffer ++ input
        if (bytes.length < string.length)
          Cont(step(bytes))
        else
          contains(bytes.sliding(string.length))
      case Input.Empty => Cont(step(buffer))
      case Input.EOF => Done(false, Input.EOF)
    }
    Cont(step(Array.empty))
  }

}
