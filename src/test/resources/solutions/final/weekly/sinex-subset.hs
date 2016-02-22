#!/usr/bin/env stack
-- stack --install-ghc runghc --package turtle

{-# LANGUAGE LambdaCase        #-}
{-# LANGUAGE OverloadedStrings #-}

import qualified Data.Text as T
import qualified Data.Text.IO as TIO
import Data.Text.Read as T (decimal)
import Turtle

sinexDir :: T.Text
sinexDir = "/nas/gemd/geodesy_data/gnss/solutions/final/weekly/"

filterSinexFiles :: Integer -> Integer -> [Text] -> IO ()
filterSinexFiles start end files = mapM_ TIO.putStrLn (filter p files)
  where
      week :: Text -> Integer
      week file = case T.decimal (T.drop (T.length sinexDir + 3) file) of
                      Right (n, _) ->  n
                      Left _       -> -1
      p file = let n = week file in (n >= start) && (n <= end)

main :: IO ()
main = getFiles >>= filterSinexFiles 15647 16165
  where
    getFiles = arguments >>= \case
        [] -> T.lines . snd <$> shellStrict ("ls " <> sinexDir <> "*.SNX") empty
        as -> return as

